package me.retrodaredevil.game.trackshooter.render.selection.options.providers;

import me.retrodaredevil.controller.options.ControlOption;

public class PageControlOptionVisibility implements ConfigurableObjectOptionProvider.ControlOptionVisibility {
	private Page currentPage = Page.MAIN;

	public void setPage(Page page){
		this.currentPage = page;
	}

	@Override
	public boolean shouldShow(ControlOption controlOption) {
		String[] split = controlOption.getCategory().split("\\.");
		if(split.length < 2){
			return currentPage == Page.MISC;
		}
        return Page.getPage(split[1]) == currentPage;
	}
	public enum Page {
		MAIN("main"), MOVEMENT("movement"), ROTATION("rotation"), SHOOTING("shooting"), MISC("misc");

		private final String name;
		Page(String name){
			this.name = name;
		}
		String getName(){
			return name;
		}
		public static Page getPage(String name){
			for(Page page : values()){
				if(page.getName().equalsIgnoreCase(name)){
					return page;
				}
			}
			return MISC;
		}
	}
}
