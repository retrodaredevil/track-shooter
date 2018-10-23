package me.retrodaredevil.game.trackshooter.render.selection;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Pools;

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

	public ContainerSingleOption(Size size){
		this.size = size;
	}
	public Size getSize(){
		return size;
	}

	protected void onInit(Table container){}
	protected void onUpdate(Table container){}

	@Override
	public void renderUpdate(Table table) {
		if(!initialized){
			onInit(container);
			initialized = true;
		}

		if(container.getParent() != table){
			containerCell = table.add(container);
			if(size.hasWidth()){
				containerCell.width(size.requireWidth());
			}
			if(size.hasHeight()){
				containerCell.height(size.requireHeight());
			}
			table.row();
		}

		onUpdate(container);
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
