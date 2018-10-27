package me.retrodaredevil.game.trackshooter.render.selection;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.Collection;

import me.retrodaredevil.game.trackshooter.util.Size;

/**
 * A SingleOption which provides some implementation and uses a "container" to add actors to instead
 * of the table itself.
 */
public abstract class ContainerSingleOption implements SingleOption {
	private final Table container = new Table();
	private final Size size;
	private Cell containerCell = null;

	private boolean initialized = false;

	/**
	 *
	 * @param size The size that will be applied to the container. Normally, this only includes width
	 */
	public ContainerSingleOption(Size size){
		this.size = size;
	}
	public Size getSize(){
		return size;
	}

	protected void onInit(Table container){}
	protected void onUpdate(Table container){}
	protected void onRequestActions(Collection<? super SelectAction> requestedActions){}

	@Override
	public void renderUpdate(Table table, Collection<? super SelectAction> requestedActions) {
		if(!initialized){
			onInit(container);
			initialized = true;
		}

		if(container.getParent() != table){
			containerCell = table.add(container);
			size.apply(containerCell);
			table.row();
		}

		onUpdate(container);
		onRequestActions(requestedActions);
	}

	@Override
	public void reset() {
	}

	@Override
	public void remove() {
		if(!initialized){
			return;
		}
		Table table = (Table) container.getParent();
		initialized = false;

		if(table == null){
			return;
		}

		container.remove();
		// thanks for simple solution https://stackoverflow.com/a/49285366/5434860
		table.getCells().removeValue(containerCell, true);
		table.invalidate();
	}

}
