package io.dico.dicore.command;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiPredicate;

import static com.google.common.base.Preconditions.checkNotNull;

public class CommandScape {
	
	private final String[] original;
	private final List<String> proposals;
	private final HashMap<Parameter<?>, Object> parsed;
	
	public CommandScape(Parameters params, String[] original, List<String> proposals) {
		this.original = original;
		this.parsed = null;
		this.proposals = proposals;
	}
	                                                                                                                                                                     
	public CommandScape(Parameters params, String[] original) {
		this.original = original;
		this.parsed = new HashMap<>();
		this.proposals = null;
		
		String[] toParse = Arrays.copyOfRange(original, 0, params.size());
		
		for (int i = 0; i < params.size(); i++) {
			Parameter<?> param = params.get(i);
			parsed.put(param, param.accept(toParse[i]));
		}
		
		if (params.repeatsLastParameter()) {
			Parameter<?> last = params.get(params.size() - 1);
			Collection<Object> list = new ArrayList<>();
			if (original.length >= toParse.length) {
				list.add(parsed.get(last));
				for (int i = toParse.length; i < original.length; i++) {
					list.add(last.accept(original[i]));
				}
			}
			parsed.put(last, list);
		} else if (original.length > toParse.length) {
			throw new CommandException("EXEC:CommandAction.DISPLAY_" + (params.getHandler().getChildren().size() > 0 ? "HELP" : "SYNTAX"));
		}
	}
	
	public CommandScape(CommandScape toCast) {
		this.original = toCast.original;
		this.parsed = toCast.parsed;
		this.proposals = toCast.proposals;
	}
	
	public String[] original() {
		return original;
	}
	
	public List<String> proposals() {
		checkNotNull(proposals, new ConfigException("This is not a tab completer"));
		return proposals;
	}
	
	// --------------- Retrieval ---------------
	
	private static final BiPredicate<Parameter<?>, Integer> EQUAL_INDEX = (param, index) -> param.getIndex() == index;
	private static final BiPredicate<Parameter<?>, String> EQUAL_NAME = (param, name) -> param.getName().equals(name);
	
	public <T> T get(int index) {
		return get(EQUAL_INDEX, index);
	}
	
	public <T> T get(String name) {
		return get(EQUAL_NAME, name);
	}
	
	public <T> Optional<T> getOptional(int index) {
		return Optional.ofNullable(get(index));
	}
	
	public <T> Optional<T> getOptional(String name) {
		return Optional.ofNullable(get(name));
	}
	
	@SuppressWarnings("unchecked")
	private <T, U> T get(BiPredicate<Parameter<?>, U> filter, U identifier) {
		assert parsed != null : new UnsupportedOperationException();
		for (Entry<Parameter<?>, Object> entry : parsed.entrySet()) {
			if (filter.test(entry.getKey(), identifier)) {
				try {
					return (T) entry.getValue();
				} catch (ClassCastException e) {
					throw new ConfigException("Wrong class type requested for parameter " + identifier + ": " + e.getMessage());
				}
			}
		}
		throw new ConfigException("Requested parameter does not exist: " + identifier);
	}
}
