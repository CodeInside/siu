package liquibase.changelog.visitor;

import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.Database;
import liquibase.exception.LiquibaseException;

import java.util.logging.Logger;

public class UpdateVisitor implements ChangeSetVisitor {

    private Database database;

    Logger log = Logger.getLogger(getClass().getName());

    public UpdateVisitor(Database database) {
        this.database = database;
    }

    public Direction getDirection() {
        return ChangeSetVisitor.Direction.FORWARD;
    }

    public void visit(ChangeSet changeSet, DatabaseChangeLog databaseChangeLog, Database database) throws LiquibaseException {
        ChangeSet.RunStatus runStatus = this.database.getRunStatus(changeSet);
        log.fine("Running Changeset:" + changeSet);
        ChangeSet.ExecType execType = changeSet.execute(databaseChangeLog, this.database);
        if (!runStatus.equals(ChangeSet.RunStatus.NOT_RAN)) {
            execType = ChangeSet.ExecType.RERAN;
        }

        this.database.markChangeSetExecStatus(changeSet, execType);

        this.database.commit();
    }
}
