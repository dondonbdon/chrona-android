package dev.bti.chrona.androidsdk.constants;

/**
 * Represents the current state of a background synchronization job.
 */
public enum SyncStatus {
    /**
     * The calendar is not currently syncing and is ready to accept a new sync command.
     */
    IDLE,

    /**
     * A sync job is actively fetching, parsing, or updating database records.
     */
    RUNNING,

    /**
     * The last attempted sync failed (e.g., due to a network timeout or malformed ICS file).
     * The system will usually retry this automatically on the next scheduled run.
     */
    FAILED
}