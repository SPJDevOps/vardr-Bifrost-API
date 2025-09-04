package com.example.hyperloop.scheduling

enum class ScheduleType(val displayName: String, val description: String) {
    ONCE("One-time", "Execute immediately, no repetition"),
    DAILY("Daily", "Execute every day at specified time"),
    WEEKLY("Weekly", "Execute every week on specified day"),
    MONTHLY("Monthly", "Execute monthly on specified day of month"),
    MONTHLY_FIRST("First of Month", "Execute on the 1st of every month"),
    MONTHLY_LAST("Last of Month", "Execute on the last day of every month"),
    CUSTOM_INTERVAL("Custom Interval", "Execute every N days/hours/minutes"),
    CRON("Custom Cron", "Execute based on custom cron expression")
}
