package co.fxl.gui.api.template;

public interface IAsyncExecution {

	public interface ICommand {
		public boolean execute();
	}

	void execute(ICommand runnable);
}