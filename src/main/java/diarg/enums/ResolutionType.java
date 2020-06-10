package diarg.enums;

/**
 * Enumerates resolution types, i.e., whether reference independence and cautious monotony should be ensured,
 * and which approach should be used for ensuring it.
 * @author Timotheus Kampik
 */
public enum ResolutionType {
    STANDARD,
    REDUCTIONIST_REFERENCE_INDEPENDENT,
    EXPANSIONIST_REFERENCE_INDEPENDENT,
    REDUCTIONIST_CAUTIOUSLY_MONOTONIC,
    EXPANSIONIST_CAUTIOUSLY_MONOTONIC
}
