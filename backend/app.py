from flask import Flask, Response, request
from flask_cors import CORS, cross_origin

from prompt.inference import get_itinerary, get_itinerary_freeform

app = Flask(__name__)
cors = CORS(app)
app.config["CORS_HEADERS"] = "Content-Type"


@cross_origin()
@app.route("/status", methods=["GET"])
def status():
    return Response(status=200)


@cross_origin()
@app.route("/generate-itinerary", methods=["POST"])
def generate_itinerary():
    trip_location = request.json["trip_location"]
    arrival_time = request.json["arrival_time"]
    departure_time = request.json["departure_time"]
    hotel_address = request.json["hotel_address"]
    total_budget = request.json["total_budget"]

    # No parameter should be None
    if any(
        var is None
        for var in [arrival_time, departure_time, trip_location, total_budget]
    ):
        return Response("Validation error: Missing required parameters", status=400)

    output = get_itinerary(
        trip_location, arrival_time, departure_time, hotel_address, total_budget
    )

    return output.to_json()


@cross_origin()
@app.route("/generate-itinerary-freeform", methods=["POST"])
def generate_itinerary_freeform():
    trip_location = request.json["trip_location"]
    arrival_time = request.json["arrival_time"]
    departure_time = request.json["departure_time"]
    hotel_address = request.json["hotel_address"]
    total_budget = request.json["total_budget"]

    # No parameter should be None
    if any(
        var is None
        for var in [arrival_time, departure_time, trip_location, total_budget]
    ):
        return Response("Validation error: Missing required parameters", status=400)

    output = get_itinerary_freeform(
        trip_location, arrival_time, departure_time, hotel_address, total_budget
    )

    return output


if __name__ == "__main__":
    app.run(debug=True, port=8080)
