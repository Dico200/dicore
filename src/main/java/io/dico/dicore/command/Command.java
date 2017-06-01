package io.dico.dicore.command;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command extends Hierarchy<Command> {
    
    protected String acceptCall(CommandSender sender, String[] args) {
        doSenderChecks(sender);
        
        CommandAction action;
        if (args.length == 0) {
            action = CommandAction.CONTINUE;
        } else if (args[0].toLowerCase().equals("help")) {
            action = onHelpRequest;
        } else if (args[0].toLowerCase().equals("syntax")) {
            action = onSyntaxRequest;
        } else {
            action = CommandAction.CONTINUE;
        }
        return action.execute(this, sender, args);
    }
    
    protected String invokeExecutor(CommandSender sender, String[] args) {
        return execute(sender, params.toScape(args));
    }
    
    protected abstract String execute(CommandSender sender, CommandScape scape);
    
    protected List<String> acceptTabComplete(CommandSender sender, String[] args) {
        return accepts(sender) ? tabComplete(sender, params.toScape(args, complete(args))) : new ArrayList<>();
    }
    
    protected List<String> tabComplete(CommandSender sender, CommandScape scape) {
        return scape.proposals();
    }
    
    private void doSenderChecks(CommandSender sender) {
        senderType.check(sender);
        
        if (permission != null && !permission.isEmpty()) {
            Validate.isAuthorized(sender, permission);
        }
    }
    
    private List<String> complete(String[] args) {
        List<String> result = new ArrayList<>();
        if (args.length > 0) {
            String last = args[args.length - 1].toLowerCase();
            for (Command child : getChildren()) {
                if (child.getId().startsWith(last)) {
                    result.add(child.getId());
                } else {
                    for (String alias : child.getAliases()) {
                        if (alias.startsWith(last)) {
                            result.add(child.getId());
                        }
                    }
                }
            }
        }
        
        for (String s : params.complete(args)) {
            result.add(s);
        }
        return result;
    }
    
    protected Command() {
        super(Command.class);
        this.permission = "";
        this.senderType = null;
        this.description = null;
        this.helpInformation = null;
        this.aliases = null;
        this.params = null;
        this.onHelpRequest = null;
        this.onSyntaxRequest = null;
    }
    
    public Command(String command) {
        super(command, Command.class);
        this.command = command.toLowerCase();
        setPermission("$PARENT$.$COMMAND$");
        setSenderType(SenderType.EITHER);
        setDescription("");
        setHelpInformation(new String[]{});
        setAliases(new String[]{});
        setParameters(new Parameter<?>[]{});
        setOnHelpRequest(CommandAction.DISPLAY_HELP);
        setOnSyntaxRequest(CommandAction.DISPLAY_SYNTAX);
    }
    
    String command;
    private String permission;
    private SenderType senderType;
    private String description;
    private String[] helpInformation;
    private List<String> aliases;
    private Parameters params;
    private CommandAction onHelpRequest;
    private CommandAction onSyntaxRequest;
    private HelpWriter messager;
    
    @Override
    public final List<String> getAliases() {
        return aliases;
    }
    
    final String getDescription() {
        return description;
    }
    
    final boolean accepts(CommandSender sender) {
        try {
            doSenderChecks(sender);
            return true;
        } catch (CommandException e) {
            return false;
        }
    }
    
    public String getPermission() {
        return permission;
    }
    
    public SenderType getSenderType() {
        return senderType;
    }
    
    public String[] getHelpInformation() {
        return helpInformation;
    }
    
    public Parameters getParams() {
        return params;
    }
    
    public CommandAction getOnHelpRequest() {
        return onHelpRequest;
    }
    
    public CommandAction getOnSyntaxRequest() {
        return onSyntaxRequest;
    }
    
    public HelpWriter getMessager() {
        return messager;
    }
    
    protected final String collectPath(int layerFrom) {
        String[] prev = Arrays.copyOfRange(getPath(), layerFrom, getLayer() - 1);
        return String.format("%s %s", String.join(" ", prev), getId());
    }
    
    protected final String getSyntaxMessage() {
        return messager.getSyntaxMessage();
    }
    
    protected final String getHelpPage(CommandSender sender, int page) {
        return messager.parseHelpMessage(sender, page);
    }
    
    /**
     * @param permission The permission which the sender should be checked against.
     *                   Notes:
     *                   "$PARENT$" is replaced with the permission node of the parent command (you probably want a . behind it).
     *                   "$COMMAND$" is replaced with the (sub)command.
     *                   Use an empty string to disable permission checking.
     * @default "$PARENT$.$COMMAND$"
     */
    protected final void setPermission(String permission) {
        assert permission != null;
        this.permission = permission;
    }
    
    /**
     * @param senderType The type of CommandSender that should be accepted:
     *                   Player, Console, or Either.
     * @default Either
     */
    protected final void setSenderType(SenderType senderType) {
        this.senderType = senderType;
    }
    
    /**
     * @param description A brief description of what the command does
     * @default empty string
     */
    protected final void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * @param lines Lines to print when help is requested. Comes down to a very detailed description.
     * @default empty string[]
     */
    protected final void setHelpInformation(String... lines) {
        this.helpInformation = lines;
    }
    
    /**
     * The aliases for the command. Defaults to none
     *
     * @default empty NBTListImpl<String>
     */
    protected final void setAliases(String... aliases) {
        this.aliases = Arrays.asList(aliases);
    }
    
    protected final void setParameters(boolean repeatLastParameter, Parameter<?>... params) {
        this.params = new Parameters(this, params, repeatLastParameter);
    }
    
    ;
    
    protected final void setParameters(Parameter<?>... params) {
        setParameters(false, params);
    }
    
    protected final void setOnHelpRequest(CommandAction action) {
        this.onHelpRequest = action;
    }
    
    protected final void setOnSyntaxRequest(CommandAction action) {
        this.onSyntaxRequest = action;
    }
    
    @Override
    public final Command getParent() {
        Hierarchy<Command> parent = super.getParent();
        while (parent.isPlaceHolder()) {
            parent = parent.getParent();
        }
        return parent.getInstance();
    }
    
    @Override
    final void setParent(Hierarchy<Command> parent) {
        super.setParent(parent);
        Command inst = getParent();
        if (inst.permission.isEmpty()) {
            this.permission = permission.replace("$PARENT$.", "").replace(".$PARENT$", "");
        }
        this.permission = permission.replace("$PARENT$", inst.permission).replace("$COMMAND$", getId());
        this.messager = new HelpWriter(this, command, helpInformation, aliases, params.syntax());
    }
    
    @Override
    protected boolean addChild(String id, Hierarchy<Command> instance) {
        if (super.addChild(id, instance)) {
            if (messager != null)
                messager.addSubcommandHeader();
            return true;
        }
        return false;
    }
    
}
