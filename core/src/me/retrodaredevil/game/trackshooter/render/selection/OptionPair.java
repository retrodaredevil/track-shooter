package me.retrodaredevil.game.trackshooter.render.selection;


/**
 * A simple class that represents a {@link SingleOption} and the {@link SingleOptionProvider} that "provided" it
 */
public final class OptionPair {
	private final SingleOption singleOption;
	private final SingleOptionProvider provider;

	public OptionPair(SingleOption singleOption, SingleOptionProvider provider){
		this.singleOption = singleOption;
		this.provider = provider;
	}
	public SingleOption getSingleOption(){ return singleOption; }
	public SingleOptionProvider getProvider(){ return provider; }
}

