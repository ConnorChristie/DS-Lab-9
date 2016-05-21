package lab9.christieck;

import java.util.Stack;

/**
 * A undo / redo stack, holds the previous events and undoes / redoes them
 *
 * @param <E> The type of the value to store
 */
public class UndoRedoStack<E> extends Stack<E>
{
    private Stack undoStack;
    private Stack redoStack;

    public UndoRedoStack()
    {
        undoStack = new Stack();
        redoStack = new Stack();
    }

    /**
     * Pushes and returns the given value on the top of the stack
     *
     * @param value The value to push
     * @return The value at the top of the stack
     */
    public E push(E value)
    {
        super.push(value);

        undoStack.push("push");
        redoStack.clear();

        return value;
    }

    /**
     * Pops and returns the value at the top of the stack
     *
     * @return The popped value
     */
    public E pop()
    {
        E value = super.pop();

        undoStack.push(value);
        undoStack.push("pop");

        redoStack.clear();
        return value;
    }

    /**
     * Whether or not a undo can be done
     *
     * @return If a undo can be done
     */
    public boolean canUndo()
    {
        return !undoStack.isEmpty();
    }

    /**
     * Undoes the last stack push or pop command
     */
    public E undo()
    {
        if (!canUndo())
        {
            throw new IllegalStateException("There are no operations to undo");
        }

        Object action = undoStack.pop();

        if (action.equals("push"))
        {
            E value = super.pop();

            redoStack.push(value);
            redoStack.push("push");

            return value;
        } else
        {
            E value = (E) undoStack.pop();

            super.push(value);
            redoStack.push("pop");

            return value;
        }
    }

    /**
     * Whether or not a redo can be done
     *
     * @return If a redo can be done
     */
    public boolean canRedo()
    {
        return !redoStack.isEmpty();
    }

    /**
     * Redoes the last undone operation
     */
    public E redo()
    {
        if (!canRedo())
        {
            throw new IllegalStateException("There are no operations to redo");
        }

        Object action = redoStack.pop();

        if (action.equals("push"))
        {
            E value = (E) redoStack.pop();

            super.push(value);
            undoStack.push("push");

            return value;
        } else
        {
            E value = super.pop();

            undoStack.push(value);
            undoStack.push("pop");

            return value;
        }
    }
}