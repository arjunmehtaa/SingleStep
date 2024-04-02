from typing import List
from pydantic import BaseModel


# A Pydantic model for the itinerary of a day
class DaySchedule(BaseModel):
    day: int
    heading: str
    paragraph: str
    remaining_budget: float


# The trip itinerary will just be a list of those day itineraries
class Itinerary(BaseModel):
    itinerary: List[DaySchedule]

    def to_json(self):
        return {
            "itinerary": [day_schedule.model_dump() for day_schedule in self.itinerary]
        }
