# backstep

Backend server for Singlestep to help integrate with GPT-4.

## Key

`key.txt` is a text file in the root dir where you'll store the OpenAI key. If you don't have a key, ask me (Chris) for mine.

## Endpoints

`/generate-itinerary` and `/generate-itinerary-freeform` are the two endpoints of concern. One generates you a JSON-formatted itinerary, and another generates you an HTML formatted itinerary. They both take in the same input:

```
{
  "trip_location": "New York City",
  "arrival_time": "2023-03-29 07:35:00",
  "departure_time": "2023-04-03 19:22:23",
  "hotel_address": "13 W 24th St, New York, NY 10001, United States",
  "total_budget": 5000
}
```

The former returns the itinerary as a list of day itineraries in JSON, providing a more structured format that may be less error prone(?).

Freeform, the latter takes much less time to process and returns an HTML format, which could be used almost immediately in Singlestep.

You can see the structure of the responses in `example_responses`.

## Notes

Sometimes the response is just wrong. Validation is going to be extremely wonky since GPT is a black box, and essentially we have no idea what prompts make it tick and what makes or breaks it.

I believe freeform is faster; we can use that first.
