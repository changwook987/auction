import org.w3c.dom.HTMLFormElement
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.dom.events.ChangeEventHandler
import react.dom.events.FormEventHandler
import react.dom.html.InputType
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.input
import react.useState

external interface UserInputProps : Props {
    var onSubmit: (String) -> Unit
}

val userInput = FC<UserInputProps> { props ->
    val (text, setText) = useState("")
    val submitHandler: FormEventHandler<HTMLFormElement> = {
        it.preventDefault()
        setText("")
        props.onSubmit(text)
    }
    val changeHandler: ChangeEventHandler<HTMLInputElement> = {
        setText(it.target.value)
    }
    form {
        onSubmit = submitHandler
        input {
            type = InputType.text
            onChange = changeHandler
            value = text
        }
    }
}