package actions;

import game.GameState;
import token.Firefighter;
import token.POI;
import token.Speciality;

public class PickOrDrop extends Action {

	private static final long serialVersionUID = 1L;
	protected ActionList title;
	protected POI healedVictim;

	public PickOrDrop(POI healedVictim) {
		APcost = 0;
		this.healedVictim = healedVictim;
	}

	@Override
	public void perform(GameState gs) {
		Firefighter f = gs.getPlayingFirefighter();
		if (title == ActionList.Drop){
			f.setFollow(null);
			healedVictim.setResuscitate(null);
		}
		else {
			f.setFollow(healedVictim);
			healedVictim.setResuscitate(f);
		}
	}

	@Override
	public boolean validate(GameState gs) {
		Firefighter f = gs.getPlayingFirefighter();
		if (f.getSpeciality() == Speciality.PARAMEDIC) {
			if (f.getFollow() != null) { //to drop
				title = ActionList.Drop;
				return true;
			}
			else if (f.getFollow() == null && healedVictim.isResuscitated()) { //to pick
				title = ActionList.Pick;
				return true;
			}
		}
		return false;
	}

	@Override
	public void adjustAction(GameState gs) {

	}
	
	public ActionList getTitle() {
		System.out.println(this.title);
    	return this.title;
    }

}
