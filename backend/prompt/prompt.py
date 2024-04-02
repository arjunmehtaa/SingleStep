from enum import Enum
from typing import List

from .prompt_builder import Prompt, Role, prompt_builder


class SystemPrompts(Enum):
    itinerary = """You are a seasoned local guide of a trip's location, and you are asked to create an itinerary of events, given an arrival time and departure time, hotel location, and total budget. Write a heading and paragraph body for each day describing the events in detail, and note the remaining budget at the end of each day. Mention at least one specific restaurant each day. Estimate the cost of the events and subtract it from the total budget as the days go by. Each paragraph must have AT LEAST 70 words, heading should not be more than 6 words. Your output must conform to the following JSON schema:
    {"itinerary" : [{
    "day": <calculate>,
    "heading": "<generate this>",
    "paragraph": "<generate this>",
    "remaining_budget": <calculate>
    },
    ...]}
    """

    itinerary_freeform = """Please suggest the best itinerary for my trip, formatted in a day-by-day manner (Day 1, Day 2, etc.). The itinerary should be in human-readable, intriguing and explanatory sentences, and must have over 40 words. The response must be returned in an HTML format; give each paragraph a <p> tag and use <h2> tag for the headings.

        The itinerary must include:
        - At least one specific restaurant recommendation for each day.
        - One hotel check-in and check-out on the first and last day."""


# Builds the dynamic portion of the prompt
def create_itinerary_prompt(
    location: str,
    arrival_time: str,
    departure_time: str,
    hotel_address: str,
    budget: float,
) -> str:
    out = [f"Trip Location: {location}"]
    out.append(
        "Flight arrival time: "
        + arrival_time
        + "\n"
        + "Flight departure time: "
        + departure_time
    )
    out.append(f"Hotel address: {hotel_address}")
    out.append(f"Total budget allotted: {budget}")
    return "\n".join(out)


# Constructs prompt to feed into GPT
def get_itinerary_prompt(
    location: str,
    arrival_time: str,
    departure_time: str,
    hotel_address: str,
    budget: float,
    html_response=False,
) -> Prompt:
    prompt = prompt_builder()
    prompt.set_system_prompt(
        SystemPrompts.itinerary_freeform.value
        if html_response
        else SystemPrompts.itinerary.value
    )

    prompt.add_message(
        Role.USER,
        create_itinerary_prompt(
            location, arrival_time, departure_time, hotel_address, budget
        ),
    )
    return prompt
