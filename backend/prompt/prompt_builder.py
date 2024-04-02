import json
from enum import Enum


class Role(Enum):
    SYSTEM = "system"
    ASSISTANT = "assistant"
    USER = "user"


def _create_message(role: Role, content):
    return {"role": role.value, "content": content}


class Prompt:
    def __init__(self):
        self.messages = [_create_message(Role.SYSTEM, "You are a helpful assistant.")]

    def set_system_prompt(self, system_prompt):
        self.messages[0] = _create_message(Role.SYSTEM, system_prompt)

    def add_message(self, role, content):
        self.messages.append(_create_message(role, content))

    def get_messages(self):
        return self.messages

    def get_prettified_str(self) -> str:
        return "\n".join([f"**{m['role']}:** {m['content']}  " for m in self.messages])

    def load_from_json(self, filename):
        with open(filename, "r") as f:
            self.messages = json.load(f)

    def load_from_text(self, filename):
        new_messages = []
        current_message = dict(role=None, content="")

        def evaluate_line(line):
            if line.startswith(Role.SYSTEM.value + ": "):
                return Role.SYSTEM.value, line[len(Role.SYSTEM.value + ": ") :]
            elif line.startswith(Role.ASSISTANT.value + ": "):
                return Role.ASSISTANT.value, line[len(Role.ASSISTANT.value + ": ") :]
            elif line.startswith(Role.USER.value + ": "):
                return Role.USER.value, line[len(Role.USER.value + ": ") :]
            else:
                return None, line

        def update_and_append_current_message(role, content):
            if current_message["role"]:
                current_message["content"] = current_message["content"].strip()
                new_messages.append(current_message.copy())
            current_message["role"] = role
            current_message["content"] = content

        with open(filename, "r") as f:
            for line in f:
                role, content = evaluate_line(line)
                if role:
                    update_and_append_current_message(role, content)
                else:
                    current_message["content"] += content
                    continue

        # Append the last message
        if current_message["role"]:
            new_messages.append(current_message)

        self.messages = new_messages


def prompt_builder() -> Prompt:
    return Prompt()
