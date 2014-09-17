/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package calcassignment;

import java.awt.geom.Point2D;
import java.net.URL;
import java.util.EmptyStackException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Stack;

/**
 * This class is the controller class for the Calculator Java FX GUI
 * 
 * 
 * @author David Kavanagh
 */
public class CalculatorController implements Initializable {

	@FXML
	private Label label;

	@FXML
	private Button buttonC;

	@FXML
	private Button buttonLeftBracket;

	@FXML
	private Button buttonRightBracket;

	@FXML
	private Button buttonMult;

	@FXML
	private Button buttonAdd;

	@FXML
	private Button buttonSub;

	@FXML
	private Button buttonDiv;

	@FXML
	private Button buttonEquals;

	@FXML
	private Button buttonPoint;

	@FXML
	private Button buttonDelete;

	@FXML
	private Button button0;

	@FXML
	private Button button1;

	@FXML
	private Button button2;

	@FXML
	private Button button3;

	@FXML
	private Button button4;

	@FXML
	private Button button5;

	@FXML
	private Button button6;

	@FXML
	private Button button7;

	@FXML
	private Button button8;

	@FXML
	private Button button9;

	@FXML
	private Text display = new Text("0");

	private String displayText = "", postfix = "", answer, infix = "";

	private boolean bracketTest = false;

	private Stack<String> opStack = new Stack<String>();

	private Stack<String> stack = new Stack<String>();

	/**
	 * handleButtonCAction() - Handles the clear button action. Clears all the
	 * strings, the stacks and sets the display to 0
	 * 
	 * @param event
	 */
	@FXML
	private void handleButtonCAction(ActionEvent event) {
		displayText = "";
		postfix = "";
		answer = "";
		infix = "";

		stack.clear();
		opStack.clear();

		display.setText("0");
	}

	/**
	 * handleButtonDelete() - Handles the delete button action. Allows the user
	 * to delete numbers or operators that they have entered
	 * 
	 * @param event
	 */
	@FXML
	private void handleButtonDelete(ActionEvent event) {
		try {
			displayText = displayText.substring(0, displayText.length() - 1);

			display.setText(displayText);

			// if the last entry by the user was a number delete it from the
			// infix string
			if (isNumber(infix.substring(infix.length() - 1))) {
				infix = infix.substring(0, infix.length() - 1);
				// else the last entry will be an operator, so delete the
				// operator plus the two regex either side of the operator
			} else {
				infix = infix.substring(0, infix.length() - 3);
			}
		} catch (StringIndexOutOfBoundsException e) {
			System.out.print(e);
		}
	}

	/**
	 * handleButtonLeftBracketAction() - Handles the event when the button is
	 * pressed for the left bracket. Checks to see if an operator has been
	 * placed before the bracket. If not a multiplication operator is added to
	 * the infix string
	 * 
	 * @param event
	 */
	@FXML
	private void handleButtonLeftBracketAction(ActionEvent event) {
		displayText += "(";
		display.setText(displayText);

		// if the user has not entered an operator before the brackets, or the
		// last entry was a closing bracket the operation inside the brackets
		// must be multiplied by the previous number
		if (!infix.equals("")) {
			if (isNumber(infix.substring(infix.length() - 1))
					|| infix.substring(infix.length() - 2).equals(")_")) {

				infix += "_*_(_";
			} else {

				infix += "_(_";
			}
		} else {
			infix += "_(_";
		}
	}

	/**
	 * handleButtonRightBracketAction() - Handles the event when the button is
	 * pressed for the right bracket. The infix string is split and checked to
	 * ensure that a opening bracket is there, if not an error message shows on
	 * the calculator.
	 * 
	 * @param event
	 */
	@FXML
	private void handleButtonRightBracketAction(ActionEvent event) {
		String[] test = infix.split("_");

		if (infix.equals("")) {
			bracketTest = true;
		} else {
			for (int i = 0; i < test.length; i++) {
				String check = test[i];
				if (check.equals("(")) {

					bracketTest = false;
					break;
				} else {
					bracketTest = true;
				}
			}
		}

		if (!bracketTest) {
			displayText += ")";
			display.setText(displayText);

			infix += "_)_";
		} else {
			handleButtonCAction(event);
			display.setText("Invalid input");
		}
	}

	/**
	 * handleButtonMult() - Handles the action for the multiplication operator
	 * button. If the user presses this button before any number has been
	 * entered or after another operator nothing will happen.
	 * 
	 * @param event
	 */
	@FXML
	private void handleButtonMult(ActionEvent event) {
		if (!doubleOperator() && !(infix.equals(""))) {
			displayText += "x";
			display.setText(displayText);

			infix += "_*_";
		}
	}

	/**
	 * handleButtonAdd() - Handles the action for the addition operator button.
	 * If the user presses this button before any number has been entered or
	 * after another operator nothing will happen.
	 * 
	 * @param event
	 */
	@FXML
	private void handleButtonAdd(ActionEvent event) {
		if (!doubleOperator() && !(infix.equals(""))) {
			displayText += "+";
			display.setText(displayText);

			infix += "_+_";
		}
	}

	/**
	 * handleButtonSub() - Handles the action for the subtraction operator
	 * button. Allows for the user to enter minus numbers.
	 * 
	 * @param event
	 */
	@FXML
	private void handleButtonSub(ActionEvent event) {

		displayText += "-";
		display.setText(displayText);

		if (infix.equals("") || !isNumber(infix.substring(infix.length() - 1))) {
			infix += "-_";
		} else {
			infix += "_-_";
		}

	}

	/**
	 * handleButtonDiv() - Handles the action for the division operator button.
	 * If the user presses this button before any number has been entered or
	 * after another operator nothing will happen.
	 * 
	 * @param event
	 */
	@FXML
	private void handleButtonDiv(ActionEvent event) {
		if (!doubleOperator() && !(infix.equals(""))) {
			displayText += "/";
			display.setText(displayText);

			infix += "_/_";
		}
	}

	@FXML
	private void handleButtonPoint(ActionEvent event) {
		displayText += ".";
		display.setText(displayText);

		infix += ".";
	}

	@FXML
	private void handleButton0(ActionEvent event) {
		displayText += "0";
		display.setText(displayText);

		infix += "0";
	}

	@FXML
	private void handleButton1(ActionEvent event) {
		displayText += "1";
		display.setText(displayText);
		infix += "1";
	}

	@FXML
	private void handleButton2(ActionEvent event) {
		displayText += "2";
		display.setText(displayText);
		infix += "2";

	}

	@FXML
	private void handleButton3(ActionEvent event) {
		displayText += "3";
		display.setText(displayText);
		infix += "3";
	}

	@FXML
	private void handleButton4(ActionEvent event) {
		displayText += "4";
		display.setText(displayText);
		infix += "4";
	}

	@FXML
	private void handleButton5(ActionEvent event) {
		displayText += "5";
		display.setText(displayText);
		infix += "5";
	}

	@FXML
	private void handleButton6(ActionEvent event) {
		displayText += "6";
		display.setText(displayText);
		infix += "6";
	}

	@FXML
	private void handleButton7(ActionEvent event) {
		displayText += "7";
		display.setText(displayText);
		infix += "7";
	}

	@FXML
	private void handleButton8(ActionEvent event) {
		displayText += "8";
		display.setText(displayText);
		infix += "8";
	}

	@FXML
	private void handleButton9(ActionEvent event) {
		displayText += "9";
		display.setText(displayText);
		infix += "9";
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		
	}

	/**
	 * handleButtonEquals() - Handles an event when the equals button is pressed
	 * on the calculator GUI. Takes the postfix string and splits it into
	 * individual elements which are passed into a postfixArray. Iterates
	 * through the array, pushing any numbers onto a stack an operator element
	 * is selected from the array. When this happens, the last 2 numbers that
	 * have been pushed onto the stack are popped off, the operation is
	 * performed on them and the result of that operation is pushed back onto
	 * the stack. This continues until only one element is left on the stack.
	 * That element is the answer which is placed onto the screen of the
	 * calulator GUI
	 * 
	 * @param event
	 */
	@FXML
	private void handleButtonEquals(ActionEvent event) {

		// convert the infix string into a postfix string
		postfixConversion();

		try {
			// divide the postfix string into separate string objects and place
			// them into a postfixArray
			String[] postfixArray = postfix.split("_");

			double result = 0.0;

			for (int y = 0; y < postfixArray.length; y++) {

				System.out.print("\n Array: " + postfixArray[y]);
			}

			for (int i = 0; i < postfixArray.length; i++) {

				// if the element in the array is a number then push it onto the
				// stack
				if (isNumber(postfixArray[i])) {

					stack.push(postfixArray[i]);

					// else if the element in the array is an operator
				} else if (postfixArray[i].equals("+")
						|| postfixArray[i].equals("-")
						|| postfixArray[i].equals("*")
						|| postfixArray[i].equals("/")) {

					System.out.print("\n Stack : " + stack);

					// if the element in the array is *, pop the top two
					// elements from the stack and multiply them. Then pop the
					// result back onto the stack
					if (postfixArray[i].equals("*")) {

						Double rs = Double.parseDouble(stack.pop());
						Double ls = Double.parseDouble(stack.pop());

						result = ls * rs;

						stack.push(Double.toString(result));

						System.out.print("\n Mult: " + stack);

					} else if (postfixArray[i].equals("/")) {

						Double rs = Double.parseDouble(stack.pop());
						Double ls = Double.parseDouble(stack.pop());

						// if a number / by 0, cancel the operation and print
						// "infinity" to the screen
						if (rs == 0) {
							displayText = "";
							display.setText("Infinity");
							postfix = "";
							infix = "";
							stack.clear();
							opStack.clear();
							return;
						}
						result = ls / rs;

						stack.push(Double.toString(result));

						System.out.print("\n Div: " + stack);

					} else if (postfixArray[i].equals("+")) {

						Double rs = Double.parseDouble(stack.pop());
						Double ls = Double.parseDouble(stack.pop());

						result = ls + rs;

						stack.push(Double.toString(result));

						System.out.print("\n Add: " + stack);

					} else if (postfixArray[i].equals("-")) {

						Double rs = Double.parseDouble(stack.pop());
						Double ls = Double.parseDouble(stack.pop());

						result = ls - rs;

						stack.push(Double.toString(result));

						System.out.print("\n Sub: " + stack);

					}
				}

			}

			System.out.print("\n" + result);

			// the answer will be the last element in the stack
			answer = stack.pop();

			// if the answer is an integer, split it and remove the decimal
			// point before it is displayed
			if (isInt(answer)) {

				String[] check = answer.split("\\.");
				answer = check[0];
				// display the answer on the calculator GUI and clear all the
				// fields
				displayText = "";
				display.setText(answer);
				postfix = "";
				infix = "";
				stack.clear();
				opStack.clear();
			} else {
				displayText = "";
				display.setText(answer);
				postfix = "";
				infix = "";
				stack.clear();
				opStack.clear();
			}
		} catch (EmptyStackException e) {
			System.out.print("Empty stack exception occured: " + e);
		}
	}

	/**
	 * postfixConversion() - This method will take the infix string, use the
	 * String.split() method to split it into individual elements and place them
	 * into a String Array named conversion. A for loop and an operator stack
	 * are then used to take each individual element from the conversion Array
	 * and place them back into a String in postfix order
	 * 
	 * 
	 */
	private void postfixConversion() {

		System.out.print("Infix :" + infix + "\n");

		try {
			// String.split() method uses a regex, in this case "_" to split up
			// a string into individual elements and place them into a String
			// Array
			String[] conversion = testForNegativeNumbers();

			// call the method to test for both brackets. If it returns true
			// then the user has not entered a closing bracket so clear all the
			// fields and print an invalid input message
			if (testForBothBrackets(conversion)) {
				displayText = "";
				postfix = "";
				infix = "";
				stack.clear();
				opStack.clear();
				display.setText("Invalid input");
				return;
			}

			for (int i = 0; i < conversion.length; i++) {

				if (conversion[i].equals("")) {
					continue;
				}

				// if the element is a number, push it straight onto the stack
				if (isNumber(conversion[i])) {

					postfix += conversion[i] + "_";

					System.out.print("\n Iteration" + i + " :" + postfix);

					// ----------------------------
					// if the operator is (
					// ----------------------------

				} else if (conversion[i].equals("(")) {

					opStack.push("(");

					// ----------------------------
					// if the operator is )
					// ----------------------------

				} else if (conversion[i].equals(")")) {

					// pop everything that has been placed on the after the left
					// side bracket
					while (opStack.lastElement() != "(") {
						postfix += opStack.pop() + "_";
					}

					// pop off the left side bracket thats left on the stack
					opStack.pop();

					// ----------------------------
					// if the operator is * , / , + , -
					// ----------------------------
				} else if (conversion[i].equals("*")
						|| conversion[i].equals("/")
						|| conversion[i].equals("+")
						|| conversion[i].equals("-")) {

					// if the stack is empty or the the operators on the stack
					// have lesser precedence push it on
					if (opStack.isEmpty()
							|| compareTo(conversion[i]) > compareTo(opStack
									.lastElement())) {

						opStack.push(conversion[i]);

						// else if the operators on the stack have higher or
						// equal precedence, pop the elements onto the postfix
						// string and push the element onto the stack

					} else if (compareTo(conversion[i]) <= compareTo(opStack
							.lastElement())) {

						// while the stack is not empty and the operator has
						// lesser or equal precedence than the operators on the
						// stack and the top element is not the left side those
						// operators off the stack and on to the postfix
						// string
						while (!opStack.empty()
								&& compareTo(conversion[i]) <= compareTo(opStack
										.lastElement())
								&& !opStack.lastElement().equals("(")) {

							postfix += opStack.pop() + "_";
						}

						// push the operator onto the stack
						opStack.push(conversion[i]);
					}

				}
			}

			// if anything is left on the stack after the conversions, pop it
			// onto the end of the postfix string
			while (!opStack.isEmpty()) {

				postfix += opStack.pop() + "_";
			}
		} catch (EmptyStackException e) {
			System.out.print("Empty stack exception occured: " + e);
		}

		System.out.print("Postfix: " + postfix + "\n");
	}

	/**
	 * doubleOperator() - Takes the infix string and splits it into individual
	 * pieces that are placed into a opCheck Array. If the last string in the
	 * array is an operator, return true
	 * 
	 * @return boolean
	 */
	private boolean doubleOperator() {
		if (!infix.equals("")) {
			String[] opCheck = infix.split("_");

			String check = opCheck[opCheck.length - 1];

			if (check.equals("*") || check.equals("/") || check.equals("+")) {
				return true;
			}
		}

		return false;
	}

	/**
	 * testForBothBrackets() - Takes the conversion array from the
	 * postfixConversion() method as a parameter. Iterates through the list to
	 * see if an opening bracket is there without a closing bracket. Returns
	 * true if there is
	 * 
	 * @param str
	 *            - a String Array
	 * 
	 * @return boolean
	 */
	private boolean testForBothBrackets(String[] str) {
		boolean left = false;
		boolean right = false;

		for (int i = 0; i < str.length; i++) {
			if (str[i].equals("(")) {
				left = true;
			}
			if (str[i].equals(")")) {
				right = true;
			}
		}

		if ((left && !right)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * isInt() - Takes in a string as a parameter, splits the string into two
	 * halves using the decimal point to split it. If the string after the
	 * decimal point is 0, then the number can be displayed as an int so return
	 * true
	 * 
	 * @param str
	 *            - a string representation of an operand or an operator
	 * 
	 * @return - boolean
	 */
	public boolean isInt(String str) {

		String[] check = str.split("\\.");

		String test = check[1];

		if (test.equals("0")) {

			return true;
		}

		return false;

	}

	/**
	 * testForNegativeNumber() - Splits the infix string and places it into a
	 * test array. Iterates through the Array and uses a counter to check if
	 * there is two operators in a row. If there is, it is a minus so remove the
	 * minus from the list and change the number after it to be a minus number
	 * 
	 * @return test - The String array made up of the characters of the infix
	 *         string
	 */
	private String[] testForNegativeNumbers() {

		String[] test = infix.split("_");

		if (test[0].equals("-") && isNumber(test[1])) {
			test[1] = "-" + test[1];
			test[0] = "_";
		}

		int counter = 0;

		for (int i = 0; i < test.length; i++) {

			if (test[i].equals("")) {
				continue;
			}

			if (test[i].equals("*") || test[i].equals("+")
					|| test[i].equals("-") || test[i].equals("/")) {
				counter++;
			} else {
				counter = 0;
			}

			if (counter == 2) {
				System.out.print("\n Counter 2: " + test[i]);
				test[i] = "-" + test[i + 1];
				test[i + 1] = "_";
				counter = 0;
			}
		}

		return test;

	}

	/**
	 * isNumber() - Takes in a string as a parameter, attempts to parse the
	 * string into a double. If the parsing is successful it returns true, if
	 * unsuccessful returns.
	 * 
	 * @param str
	 *            - a string representation of an operand or an operator
	 * 
	 * @return - boolean
	 */
	public boolean isNumber(String str) {

		try {
			double x = Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}

	}

	/**
	 * compareTo() - Takes in a string as a parameter, the string is a
	 * representation of an operator. It will return an integer value based on
	 * the level of precedence for each operator.
	 * 
	 * @param s
	 *            - a string representation of an operator
	 * 
	 * @return - integer value for each operator
	 */
	public int compareTo(String s) {
		if (s.equals("(") || s.equals(")"))
			return 0;
		if (s.equals("*"))
			return 4;
		if (s.equals("/"))
			return 3;
		if (s.equals("+"))
			return 2;
		if (s.equals("-"))
			return 2;

		return 0;
	}
}
