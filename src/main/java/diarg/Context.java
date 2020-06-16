package diarg;

import net.sf.tweety.arg.dung.semantics.Extension;

/**
 * A context wrapper that allows for the exclusion of the specified arguments from an argumentation framework
 * @author Timotheus Kampik
 */

public class Context {
    private String name;
    private Extension arguments;

    public String getName() {
        return name;
    }

    public Extension getArguments() {
        return arguments;
    }

    public void setArguments(Extension arguments) {
        this.arguments = arguments;
    }

    public Context (String name, Extension arguments){
        this.name = name;
        this.arguments = arguments;
    }
}
