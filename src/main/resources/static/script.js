

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
                    li.classList.add(phoneNumber.specialPhoneNumberType.toLowerCase());

                    if (phoneNumber.specialPhoneNumberPrice > 0) {
                       const priceTag = document.createElement("span");
                       priceTag.classList.add("price-tag");
                       priceTag.textContent = `${phoneNumber.specialPhoneNumberPrice} TL`;
                       li.appendChild(priceTag);
                    }

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
                        redirectToConfirmPage(selectedPhoneNumberId);
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

        function redirectToConfirmPage(phoneNumberId) {
            window.location.href = `phone-numbers/confirm/${phoneNumberId}`;
        }

   /*     async function holdPhoneNumber(phoneNumberId) {
            const response = await fetch('phone-numbers/confirm/{phoneNumberId}', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({phoneNumberId: phoneNumberId })
            });

            if (response.ok) {
                window.location.href = `phone-numbers/confirm/${phoneNumberId}`;
            } else {
                alert("Failed to hold the phone number.");
            }
        }
        */
