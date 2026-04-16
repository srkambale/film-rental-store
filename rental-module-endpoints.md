# Rental Module API Endpoints Manifest

This document outlines all API endpoints for the **Rental** module in the Film Rental Store application, including associations with other tables based on the Sakila database schema.

## 1. Core Rental Endpoints
Endpoints directly managing the `rental` table.

| Method | Endpoint | Description | Status |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/rentals` | Create a new rental record. | ✅ Implemented |
| `GET` | `/api/v1/rentals` | List all rentals (with pagination). | ✅ Implemented |
| `GET` | `/api/v1/rentals/{id}` | Get detailed information for a specific rental. | ✅ Implemented |
| `PUT` | `/api/v1/rentals/{id}/return` | Record the return of a rental (sets `return_date`). | ✅ Implemented |
| `DELETE` | `/api/v1/rentals/{id}` | Delete a rental record. | ✅ Implemented |
| `PATCH` | `/api/v1/rentals/{id}` | Partially update a rental record (for testing). | ✅ Implemented |
| `GET` | `/api/v1/rentals/active` | List all currently active rentals (not yet returned). | 🛠️ Planned |
| `GET` | `/api/v1/rentals/overdue` | List all overdue rentals based on `rental_duration`. | 🛠️ Planned |

---

## 2. Customer Associations
Endpoints relating rentals to the `customer` table.

| Method | Endpoint | Description | Status |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/customers/{id}/rentals` | List search all rentals for a specific customer. | ✅ Implemented |
| `GET` | `/api/v1/customers/{id}/balance` | Calculate current balance for a customer. | 🛠️ Planned |
| `POST` | `/api/v1/customer/rentals` | Convenient endpoint for customers to rent a film. | ✅ Implemented |
| `PUT` | `/api/v1/customer/rentals/{id}/return`| Simplified return endpoint for customer view. | ✅ Implemented |

---

## 3. Inventory & Film Associations
Endpoints relating rentals to `inventory` and `film` tables.

| Method | Endpoint | Description | Status |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/inventory/available` | Check if a film is available at a specific store. | ✅ Implemented |
| `GET` | `/api/v1/inventory/{id}/rentals` | See rental history for a specific inventory item. | ✅ Implemented |
| `GET` | `/api/v1/rentals/film/{filmId}` | See rental history for a specific film. | 🛠️ Planned |
| `GET` | `/api/v1/rentals/inventory/{invId}`| See rental history for a specific inventory item. | 🛠️ Planned |
| `GET` | `/api/v1/inventory/{id}/holder` | Identify which customer currently has this item. | 🛠️ Planned |

---

## 4. Staff & Store Associations
Endpoints relating rentals to `staff` and `store` tables.

| Method | Endpoint | Description | Status |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/rentals/staff/{staffId}` | List all rentals processed by a specific staff member. | 🛠️ Planned |
| `GET` | `/api/v1/rentals/store/{storeId}` | List all rentals associated with a specific store. | 🛠️ Planned |

---

## 5. Payments & Financial Associations
Endpoints relating rentals to the `payment` table.

| Method | Endpoint | Description | Status |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/rentals/{id}/payments` | Get all payments associated with a specific rental. | 🛠️ Planned |
| `POST` | `/api/v1/rentals/{id}/payments` | Record a payment for an existing rental. | 🛠️ Planned |

---

## 6. Reports & Procedures (Analytics)
Advanced data retrieval based on views and stored procedures.

| Method | Endpoint | Description | Source |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/reports/sales/store` | Total sales per store (Manager/City/Country). | `sales_by_store` View |
| `GET` | `/api/v1/reports/sales/category` | Total sales per film category. | `sales_by_film_category` View |
| `GET` | `/api/v1/reports/customers/rewards`| Generate report for reward-eligible customers. | `rewards_report` Proc |
| `GET` | `/api/v1/reports/films/stock` | Check count of film in/out of stock. | `film_in_stock` Proc |

---

> [!NOTE]
> The endpoints marked as **✅ Implemented** are currently present in your controllers.
> The endpoints marked as **🛠️ Planned** are suggested based on the full Sakila schema logic to provide a complete Rental Module.
