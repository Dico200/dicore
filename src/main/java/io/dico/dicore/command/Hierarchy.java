package io.dico.dicore.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hierarchy<T extends Hierarchy<T>> {
	
	private Hierarchy<T> parent = null;
	private String id;
	private String[] path;
	private Class<T> type;
	private int layer;
	private List<Hierarchy<T>> children;
	
	protected Hierarchy(Class<T> type) {
		this.id = null;
		this.path = new String[]{};
		this.type = type;
		this.layer = 0;
		this.children = new ArrayList<>();
	}
	
	public Hierarchy(String path, Class<T> type) {
		String[] split = path.toLowerCase().split(" ");
		assert split.length != 0;
		this.id = split[split.length - 1];
		this.path = Arrays.copyOfRange(split, 0, split.length - 1);
		this.type = type;
		this.layer = 0;
		this.children = new ArrayList<>();
	}
	
	public T instanceAt(String[] path, boolean checkAliases) {
		assert path != null;
		if (path.length == 0)
			return getInstance();
		Hierarchy<T> next = getChild(path[0], checkAliases);
		return (next == null || next.isPlaceHolder())? getInstance() : next.instanceAt(Arrays.copyOfRange(path, 1, path.length), checkAliases);
	}
	
	public T getInstance() {
		return type.isInstance(this)? type.cast(this) : null;
	}
	
	public void addChild(T child) {
		String id = child.getId();
		String[] path = Arrays.copyOfRange(child.getPath(), getLayer(), child.getPath().length);
		if (path.length == 0) {
			addChild(id, child);
		} else {
			String key = path[0];
			if (!contains(key, false)) {
				String placeHolderPath = child.getTotalPath() + " " + id;
				addChild(id, new Hierarchy<T>(placeHolderPath, type));
			}
			getChild(key, false).addChild(child);
		}
	}
	
	protected boolean addChild(String id, Hierarchy<T> instance) {
		Hierarchy<T> otherInstance = getChild(id, false);
		if (otherInstance != null) {
			if (otherInstance.isPlaceHolder()) {
				children.remove(otherInstance);
			} else {
				return false;
			}
		}
		instance.setParent(this);
		children.add(instance);
		return true;
	}
	
	private Hierarchy<T> getChild(String id, boolean checkAliases) {
		for (Hierarchy<T> child : children)
			if (child.id.equals(id))
				return child;
		if (checkAliases)
			for (Hierarchy<T> child : children)
				if (child.getAliases().contains(id))
					return child;
		return null;
	}
	
	private boolean contains(String id, boolean checkAliases) {
		return getChild(id, checkAliases) != null;
	}
	
	public List<String> getAliases() {
		return new ArrayList<String>();
	}
	
	public int getLayer() {
		return layer;
	}
	
	public boolean isPlaceHolder() {
		return !type.isInstance(this);
	}
	
	public String getId() {
		return id;
	}
	
	public String[] getPath() {
		return path;
	}
	
	public String getTotalPath() {
		return String.join(" ", path) + " " + id;
	}
	
	protected List<T> getChildren() {
		List<T> result = new ArrayList<>();
		for (Hierarchy<T> child : this.children) {
			if (child.isPlaceHolder()) {
				result.addAll(child.getChildren());
			} else {
				result.add(child.getInstance());
			}
		}
		return result;
	}
	
	void setParent(Hierarchy<T> parent) {
		assert parent == null;
		this.parent = parent;
		this.layer = parent.layer + 1;
	}
	
	public Hierarchy<T> getParent() {
		return parent;
	}
	
}
