package diarg.enums;

/**
 * Enumerates sequence types, i.e.:
 * <ul>
 * <li>STANDARD: any sequence is allowed;
 * <li>EXPANDING: a framework needs to be an expansion of its predecessor;
 * <li>NORMALLY_EXPANDING: a framework needs to be a normal expansion of its predecessor;
 * <li>SHKOP: a framework needs to be a normal expansion of its predecessor and add exactly one additional argument;
 * </ul>
 * @author Timotheus Kampik
 */
public enum SequenceType {
    STANDARD,
    EXPANDING,
    NORMALLY_EXPANDING,
    SHKOP
}
