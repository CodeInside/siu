/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package eform;

import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import commons.Streams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


@Path("/")
final public class Api {

  final static MediaType JSON_UTF8;
  final Logger logger = Logger.getLogger(getClass().getName());

  static {
    Map<String, String> map = new HashMap<String, String>();
    map.put("charset", "UTF-8");
    JSON_UTF8 = new MediaType("application", "json", map);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getForm(@Context HttpServletRequest req, @Context HttpServletRequest response) throws UnsupportedEncodingException {
    response.setCharacterEncoding("UTF-8");
    Form form = getForm(req);
    return Response.ok(form, JSON_UTF8).header("Cache-Control", "no-cache").build();
  }


  @GET
  @Path("{id}{file:(/.*)?}")
  public Response getContent(@Context HttpServletRequest req, @PathParam("id") String id, @PathParam("file") String file) {
    Form form = getForm(req);
    Property source = form.getProperty(id);
    if (source == null || !source.attachment() || source.content == null) {
      logger.info("Not attachment " + source);
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    }
    Response.ResponseBuilder builder = Response.ok(source.content, source.mime)
      .header("Content-Length", source.content.length())
      .header("Cache-Control", "no-cache");
    if (file == null || file.isEmpty()) {
      ContentDisposition contentDisposition = ContentDisposition.type("attachment")
        .fileName(source.value)
        .modificationDate(new Date(source.content.lastModified()))
        .build();
      builder.header("Content-Disposition", contentDisposition);
    }
    return builder.build();
  }

  @POST
  @Path("plus/{name}/{newVal:[0-9]*}")
  public Response plus(@Context HttpServletRequest req, @PathParam("name") String name, @QueryParam("suffix") String suffix, @PathParam("newVal") Integer newVal) {
    Form form = getForm(req);
    String login = req.getUserPrincipal().getName();
    if (suffix == null) {
      suffix = "";
    }
    Map<String, Property> map = form.plusBlock(login, name, suffix, newVal);
    return Response.ok(map, JSON_UTF8).build();
  }

  @POST
  @Path("minus/{name}/{newVal:[0-9]*}")
  public Response minus(@Context HttpServletRequest req, @PathParam("name") String name, @QueryParam("suffix") String suffix, @PathParam("newVal") Integer newVal) {
    Form form = getForm(req);
    if (suffix == null) {
      suffix = "";
    }
    form.minusBlock(name, suffix, newVal);
    return Response.ok().build();
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response post(@Context HttpServletRequest req, FormDataMultiPart parts) {
    Form form = getForm(req);
    Map<String, List<FormDataBodyPart>> fields = parts.getFields();
    for (String field : fields.keySet()) {
      Property property = form.getProperty(field);
      if (property != null) {
        if (!property.writable) {
          continue;
        }
        List<FormDataBodyPart> formDataBodyParts = fields.get(field);
        for (FormDataBodyPart part : formDataBodyParts) {
          if (!property.attachment()) {
            property.updateValue(part.getValue());
          } else {
            FormDataContentDisposition disposition = part.getFormDataContentDisposition();
            String fileName = disposition.getFileName();
            if (fileName != null && !fileName.isEmpty()) {
              File src = part.getValueAs(File.class);
              try {
                File dst = Streams.copyToTempFile(src, "form-", ".tmp");
                property.updateContent(fileName, part.getMediaType().toString(), dst, true);
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          }
        }
      }
    }
    return Response.ok().build();
  }

  private Form getForm(HttpServletRequest req) {
    FormsHolder holder = getHolder(req);
    if (holder != null) {
      String referer = req.getHeader("Referer");
      if (referer != null) {
        String[] parts = referer.split("-", 10);
        if (parts.length >= 3) {
          Long id = Long.parseLong(parts[parts.length - 2]);
          Form form = holder.getForms().get(id);
          if (form != null) {
            return form;
          } else {
            logger.info("No form " + id);
          }
        } else {
          logger.info("Invalid referer " + referer);
        }
      }
    }
    throw new WebApplicationException(Response.Status.FORBIDDEN);
  }


  private FormsHolder getHolder(HttpServletRequest req) {
    HttpSession session = req.getSession(false);
    if (session == null) {
      logger.info("No session");
      return null;
    }
    WebApplicationContext ctx = (WebApplicationContext) session.getAttribute(WebApplicationContext.class.getName());
    if (ctx == null) {
      logger.info("No app context");
      return null;
    }
    for (Application app : ctx.getApplications()) {
      if (app instanceof FormsHolder) {
        return (FormsHolder) app;
      }
    }
    logger.info("No form holder");
    return null;
  }
}