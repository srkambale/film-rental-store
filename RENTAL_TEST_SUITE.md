# Rental Module - API Test Suite

This guide provides the sequence and payload examples for testing the Rental module.

## Step 0: Authentication (Required)
Before testing any protected endpoints, you must obtain a JWT token.

**Endpoint:** `POST /api/v1/auth/login`
**Body:**
```json
{
    "identifier": "your_username_or_email",
    "password": "your_password"
}
```
*Copy the `token` from the response and use it as `Bearer <token>` in the Authorization header for all steps below.*

---

## Step 1: Check Inventory Availability
Check if a specific film is available at a store before renting.

**Endpoint:** `GET /api/v1/inventory/available?filmId=1&storeId=1`
**Expected Response:** A list of available inventory items.

---

## Step 2: Create a New Rental
Create a rental record for a customer.

**Endpoint:** `POST /api/v1/rentals`
**Query Parameters:** `inventoryId=1`, `customerId=1`, `staffId=1`
*(Note: You can also use POST /api/v1/customer/rentals with a JSON body)*

**Alternative (Full Body):**
**Endpoint:** `POST /api/v1/customer/rentals`
**Body:**
```json
{
    "inventoryId": 1,
    "customerId": 1
}
```

---

## Step 3: Verify the Rental
Get the details of the rental you just created.

**Endpoint:** `GET /api/v1/rentals/{id}`
*(Replace {id} with the rentalId from Step 2)*

---

## Step 4: Partial Update (Testing Patch)
Update a field manually for testing purposes.

**Endpoint:** `PATCH /api/v1/rentals/{id}`
**Body (Update Staff):**
```json
{
    "staffId": 2
}
```

---

## Step 5: Process a Return
Record that the customer has returned the film.

**Endpoint:** `PUT /api/v1/rentals/{id}/return`
**Expected Response:** The rental object with a populated `returnDate`.

---

## Step 6: View Customer History
Verify that the rental appears in the customer's history.

**Endpoint:** `GET /api/v1/customers/{id}/rentals`

---

## Step 7: Delete (Cleanup)
Remove a test rental record.

**Endpoint:** `DELETE /api/v1/rentals/{id}`

---

## Step 8: View Inventory History
Check who has rented a specific physical copy over time.

**Endpoint:** `GET /api/v1/inventory/{id}/rentals`
