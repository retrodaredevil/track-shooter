package me.retrodaredevil.game.trackshooter.item;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import me.retrodaredevil.game.trackshooter.effect.Effect;
import me.retrodaredevil.game.trackshooter.entity.Entity;

public abstract class EffectItem implements DisplayedItem {

	private final Image image;
	private boolean used = false;

	public EffectItem(Image image){
		this.image = image;
	}

	/**
	 * By calling this, it will add the effect created with createEffect() (if possible) and will make the next time
	 * isUsed() is called the return value
	 * @param entity The entity to (possibly) add the effect to)
	 * @return true if the effect was added. If true, will return true in isUsed()
	 */
	protected boolean activateEffect(Entity entity){
		if(used){
			throw new IllegalStateException("Item: " + toString() + " was not removed.");
		}
		Effect effect = createEffect(entity);
		if(effect == null){
			return false;
		}
		entity.addEffect(effect);
		used = true;
		return true;
	}

	/**
	 *
	 * @return The effect to add or null if the effect cannot be added
	 */
	protected abstract Effect createEffect(Entity entity);

	@Override
	public boolean isUsed() {
		return used;
	}

	@Override
	public Image getImage() {
		return image;
	}
}
