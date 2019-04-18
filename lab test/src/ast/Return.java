package ast;

/**
 * A command for returns.
 */
public class Return extends Command implements Statement {

	private Expression arg;

	public Return(int lineNum, int charPos, Expression arg) {
		super(lineNum, charPos);
		this.arg = arg;
	}

	public Expression argument() {
		return arg;
	}

	public void accept(CommandVisitor visitor) {
		visitor.visit(this);
	}
}
