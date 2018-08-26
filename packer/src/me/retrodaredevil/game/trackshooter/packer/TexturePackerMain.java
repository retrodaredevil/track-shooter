package me.retrodaredevil.game.trackshooter.packer;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import java.io.File;

public class TexturePackerMain {

	public static void main(String[] args){
		System.out.println("Starting texture packing in directory: " + new File(".").getAbsolutePath());
		TexturePacker.process("textures", ".", "skin");
	}
}
