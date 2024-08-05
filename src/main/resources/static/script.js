

function formatPhoneNumber(phoneNumber) {
            const cleaned = ('' + phoneNumber).replace(/\D/g, '');
            const match = cleaned.match(/^(\d{3})(\d{3})(\d{2})(\d{2})$/);
            if (match) {
                return `(${match[1]}) ${match[2]} ${match[3]} ${match[4]}`;
            }
            return phoneNumber;
        }

    let selectedPhoneNumberId = null;
        async function searchPhoneNumbers(event) {
            event.preventDefault();
            const digit = document.getElementById("digit").value;
            const response = await fetch(`/phone-numbers/digit?digit=${digit}`);
            const data = await response.json();

            const resultsContainer = document.getElementById("results-container");
            resultsContainer.style.display = "block";
            const continueContainer = document.getElementById("continue-container");
            continueContainer.innerHTML = "";

            const results = document.getElementById("results");
            results.innerHTML = "";

            if (data.phoneNumbers && data.phoneNumbers.length > 0) {

                data.phoneNumbers.forEach(phoneNumber => {
                    const li = document.createElement("li");
                    li.textContent = formatPhoneNumber(phoneNumber.phoneNumber);
                    li.addEventListener('click', function() {
                        results.querySelectorAll('li').forEach(li => li.classList.remove('selected'));
                        this.classList.toggle('selected');
                        selectedPhoneNumberId = phoneNumber.phoneNumberId;
                    });
                    results.appendChild(li);
                });
                continueContainer.style.display = "block";
                const continueButton = document.createElement("button");
                continueButton.textContent = "Continue";
                continueButton.addEventListener('click', function() {
                    if (selectedPhoneNumberId) {
                        holdPhoneNumber(selectedPhoneNumberId);
                    } else {
                        alert("Please select a phone number.");
                    }
                });
                continueContainer.appendChild(continueButton);

            } else {
                continueContainer.style.display = "none";
                const noResults = document.createElement("p");
                noResults.textContent = data.result.resultDesc;
                noResults.classList.add("no-results");
                results.appendChild(noResults);

            }
        }

        async function holdPhoneNumber(phoneNumberId) {
            const response = await fetch('/phone-numbers/hold', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({phoneNumberId: phoneNumberId })
            });

            if (response.ok) {
                window.location.href = `/confirm.html?phoneNumberId=${phoneNumberId}`;
            } else {
                alert("Failed to hold the phone number.");
            }
        }