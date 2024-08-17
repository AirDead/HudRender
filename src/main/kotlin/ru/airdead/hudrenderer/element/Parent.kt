package ru.airdead.hudrenderer.element

/**
 * Interface representing a parent element in the UI.
 * Inherits from [IElement].
 */
interface Parent : IElement {

    /**
     * The list of child elements.
     */
    val children: MutableList<AbstractElement>

    /**
     * Adds a child element to the parent.
     *
     * @param element The child element to be added.
     */
    fun addChild(element: AbstractElement) {
        children.add(element)
        element.lastParent = this as? AbstractElement
    }

    /**
     * Removes a child element from the parent.
     *
     * @param element The child element to be removed.
     */
    fun removeChild(element: AbstractElement) {
        children.remove(element)
        element.lastParent = null
    }

    /**
     * Adds multiple child elements to the parent.
     *
     * @param elements The child elements to be added.
     */
    fun addChild(vararg elements: AbstractElement) {
        children.addAll(elements)
        elements.forEach { it.lastParent = this as? AbstractElement }
    }

    /**
     * Removes multiple child elements from the parent.
     *
     * @param elements The child elements to be removed.
     */
    fun removeChild(vararg elements: AbstractElement) {
        children.removeAll(elements.toSet())
        elements.forEach { it.lastParent = null }
    }

    /**
     * Adds a child element to the parent using the unary plus operator.
     *
     * @param T The type of the child element.
     *
     * @return The added child element.
     */
    operator fun <T : AbstractElement> T.unaryPlus(): T {
        this@Parent.addChild(this)
        return this
    }

    /**
     * Adds a child element to the parent using the plus operator.
     *
     * @param T The type of the child element.
     * @param element The child element to be added.
     * @return The added child element.
     */
    operator fun <T: AbstractElement> plus(element: T): T {
        this@Parent.addChild(element)
        return element
    }
}