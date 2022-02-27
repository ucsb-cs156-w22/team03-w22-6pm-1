import React, {useState} from 'react'
import { Button, Form } from 'react-bootstrap';
import { useForm } from 'react-hook-form'
import { useNavigate } from 'react-router-dom'


function EarthquakesForm({ submitAction, buttonLabel="Retrieve" }) {

    // Stryker disable all
    const {
        register,
        formState: { errors },
        handleSubmit,
    } = useForm();
    // Stryker enable all

    const navigate = useNavigate();


    return (

        <Form onSubmit={handleSubmit(submitAction)}>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="distance">Distance in km from Storke Tower</Form.Label>
                <Form.Control
                    data-testid="EarthquakesForm-distance"
                    id="distance"
                    type="text"
                    isInvalid={Boolean(errors.distance)}
                    {...register("distance", {
                        required: "Distance is required."
                    })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.distance?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="mag">Minimum magnitude of an earthquake</Form.Label>
                <Form.Control
                    data-testid="EarthquakesForm-mag"
                    id="mag"
                    type="text"
                    isInvalid={Boolean(errors.mag)}
                    {...register("mag", {
                        required: "Minimum Magnitude is required."
                    })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.mag?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Button
                type="submit"
                data-testid="EarthquakesForm-retrieve"
            >
                {buttonLabel}
            </Button>
            <Button
                variant="Secondary"
                onClick={() => navigate(-1)}
                data-testid="EarthquakesForm-cancel"
            >
                Cancel
            </Button>

        </Form>

    )
}

export default EarthquakesForm;
