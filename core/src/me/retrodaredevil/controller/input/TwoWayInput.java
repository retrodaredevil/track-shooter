package me.retrodaredevil.controller.input;

import java.util.Arrays;

import me.retrodaredevil.controller.ControlConfig;
import me.retrodaredevil.controller.ControllerPart;

public class TwoWayInput extends InputPart {
	private final InputPart part1;
	private final InputPart part2;

	/**
	 * If either of the parts have a parent, it will not be overridden. However for either or both that doesn't have
	 * a parent, its parent will be set to this.
	 * <br/>
	 * the getPosition() will work like part1.getPosition() - part2.getPosition()
	 *
	 * @param part1 The InputPart representing that will be the positive value
	 * @param part2 The InputPart that will be the negative value
	 */
	public TwoWayInput(InputPart part1, InputPart part2){
		super(part1.getAxisType().isAnalog() || part2.getAxisType().isAnalog() ? AxisType.FULL_ANALOG : AxisType.FULL_DIGITAL);
		if(part1.getAxisType().isFull() || part2.getAxisType().isFull()){
			throw new IllegalArgumentException("Neither part1 or part2's AxisType can be 'full'");
		}
		this.part1 = part1;
		this.part2 = part2;
		setParentsToThis(Arrays.asList(part1, part2), false, true);
	}
	public InputPart getPart1(){
		return part1;
	}
	public InputPart getPart2(){
		return part2;
	}

	@Override
	public void update(ControlConfig config) {
		part1.update(config);
		part2.update(config);
		super.update(config);
	}

	@Override
	protected double calculatePosition() {
		return part1.getPosition() - part2.getPosition();
	}

	@Override
	public boolean isConnected() {
		return part1.isConnected() && part2.isConnected();
	}
}
