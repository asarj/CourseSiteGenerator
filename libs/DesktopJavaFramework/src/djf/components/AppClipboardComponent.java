package djf.components;

public interface AppClipboardComponent {
    public void cut();
    public void copy();    
    public void paste();
    public boolean hasSomethingToCut();
    public boolean hasSomethingToCopy();
    public boolean hasSomethingToPaste();
}
