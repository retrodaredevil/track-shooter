package me.retrodaredevil.game.trackshooter.render;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pools;

import java.util.Objects;

public class TextActor extends Actor {
	private BitmapFont font;
	private String text;


	public TextActor(BitmapFont font, String text){
		this.font = Objects.requireNonNull(font);
		this.text = Objects.requireNonNull(text);
		updateBounds();
	}

	public void setFont(BitmapFont font){
		if(this.font == font){
			return;
		}
		this.font = Objects.requireNonNull(font);
		updateBounds();
	}
	public BitmapFont getFont(){
		return font;
	}

	public void setText(String text){
		if(this.text.equals(text)){
			return;
		}
		this.text = Objects.requireNonNull(text);
		updateBounds();
	}
	public String getText(){
		return text;
	}

	public void setText(BitmapFont font, String text){
		if(this.font == font && text.equals(this.text)){
			return;
		}
		this.font = Objects.requireNonNull(font);
		this.text = Objects.requireNonNull(text);
		updateBounds();
	}

	private void updateBounds(){
		GlyphLayout layout = Pools.obtain(GlyphLayout.class);
		layout.setText(font, text);
		setSize(layout.width, layout.height);
		Pools.free(layout);
	}

	@Override
	protected void rotationChanged() {
		throw new UnsupportedOperationException("Cannot rotate a TextActor");
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		font.draw(batch, text, getX(), getY() + getHeight());
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		return null;
	}
}
