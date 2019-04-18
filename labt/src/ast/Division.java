package ast;

/**
 * Command for divison.
 */
public class Division extends Command implements Expression {
	private Expression left;
	private Expression right;

	public Division(int lineNum, int charPos, Expression leftSide, Expression rightSide) {
		super(lineNum, charPos);
		left = leftSide;
		right = rightSide;
	}

	public Expression leftSide() {
		return left;
	}

	public Expression rightSide() {
		return right;
	}

	public void accept(CommandVisitor visitor) {
		visitor.visit(this);
	}
}
