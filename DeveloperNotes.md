# Developer Notes

## 1. API Design Choices
The original assignment describes certain behaviors that do not strictly align with standard REST conventions:
- **Using `POST`** to update resources (instead of `PUT`).
- **Returning `200 OK`** upon resource creation (instead of `201 Created`).

In normal RESTful design, we would likely opt for `PUT` (or `PATCH`) for updates and return a `201 Created` status when new resources are added. However, for this project, I chose to remain faithful to the assignment’s exact specification to avoid potential automated test failures (which might expect `200 OK` instead of `201 Created`).

## 2. Considerations on Resource Deletion
Another point I pondered: **what happens if we delete a Movie or a Showtime that already has existing bookings?** The assignment does not clarify if this action should be allowed or how to handle those existing bookings. In a real-world setting, we might:
- Prohibit deletion if any active bookings exist.
- Or mark the resource as “inactive” rather than fully removing it.
  Since the specification did not define this behavior, I adhered to the simpler approach and followed only what was explicitly stated.

## 3. Infinite Seat Capacity
In a real scenario, each theater would have a finite number of seats. The assignment, however, does not establish any seat limit. Because of that, I decided to allow any seat number without bounds. Imposing an arbitrary limit could interfere with automated tests or with the assignment’s expectations. It also avoids complications if large seat numbers were tested.

## 4. Adherence to Assignment Specifications
Overall, my implementation remains as close as possible to the stated API definitions—despite certain conventions or real-life constraints that might suggest alternative approaches. The goal was to ensure compliance with the assignment and reduce risk of failing any automated validations they might run.
