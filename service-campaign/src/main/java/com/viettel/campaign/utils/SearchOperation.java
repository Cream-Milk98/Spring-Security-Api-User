package com.viettel.campaign.utils;

public enum SearchOperation {
    EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, LIKE, STARTS_WITH, ENDS_WITH, CONTAINS;
    protected static final String[] SIMPLE_OPERATION_SET = { ":", "!", ">", "<", "~" };

    protected static final String OR_PREDICATE_FLAG = "'";

    protected static final String ZERO_OR_MORE_REGEX = "*";

    protected static final String OR_OPERATOR = "OR";

    protected static final String AND_OPERATOR = "AND";

    protected static final String LEFT_PARANTHESIS = "(";

    protected static final String RIGHT_PARANTHESIS = ")";

    public static SearchOperation getSimpleOperation(final char input) {
        switch (input) {
            case ':':
                return EQUALITY;
            case '!':
                return NEGATION;
            case '>':
                return GREATER_THAN;
            case '<':
                return LESS_THAN;
            case '~':
                return LIKE;
            default:
                return null;
        }
    }
}
