package diarg;

import net.sf.tweety.arg.dung.syntax.DungTheory;

import java.security.cert.Extension;
import java.util.Collection;
import java.util.LinkedList;

public class AFSequence {

    /* TODO also: track potential resolutions */
    private Collection<DungTheory> frameworks = new LinkedList<>();

    public AFSequence() { }

    public void addFramework(DungTheory framework) {
        /* TODO If configured: enforce expansion, normal expansion */
        frameworks.add(framework);
    }

    public void removeFramework(DungTheory framework) {
        /* TODO Check if configuration allows this */
        frameworks.remove(framework);
    }

    public Collection<Extension> resolveFramework(DungTheory framework) {
        /* TODO resolve framework based on provided config and save resolution */
        Collection<Extension> extensions = new LinkedList<>();
        return extensions;
    }

    public Collection<Extension> resolveFrameworks(DungTheory framework) {
        /* TODO resolve frameworks based on provided config and save resolution */
        Collection<Extension> extensions = new LinkedList<>();
        return extensions;
    }
}
