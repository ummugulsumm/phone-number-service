function formatPhoneNumber(phoneNumber) {
    const cleaned = ('' + phoneNumber).replace(/\D/g, '');
    const match = cleaned.match(/^(\d{3})(\d{3})(\d{2})(\d{2})$/);
    if (match) {
        return `(${match[1]}) ${match[2]} ${match[3]} ${match[4]}`;
    }
    return phoneNumber;
}

let selectedPhoneNumberId = null;
let phoneNumbers = []; // Globally store phone numbers for AI Help
let aiHelpReceived = false;

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
        phoneNumbers = data.phoneNumbers;
        document.getElementById("ai-help-button").style.display = "block";
        aiHelpReceived = false;
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

async function getAiHelp() {

    if (aiHelpReceived) {
        alert("You have already received help from AI. The recommended number is highlighted on the screen.");
        return;
    }
    const response = await fetch('/phone-numbers/ai-help', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
          body: JSON.stringify({ phoneNumbers: phoneNumbers.map(p => ({ phoneNumber: p.phoneNumber, price: p.specialPhoneNumberPrice })) })
    });
    const textResponse = await response.text();
    displayAiResponse(textResponse);

    aiHelpReceived = true;
}

function displayAiResponse(textResponse) {
    const aiResponseModel = document.getElementById("ai-response-modal");
    aiResponseModel.style.display = "block";
    const aiResponse = document.getElementById("ai-response");
    aiResponse.innerHTML = parseMarkdown(textResponse);

    const recommendedNumber = phoneNumbers.find(p => textResponse.includes(p.phoneNumber)).phoneNumber;
    const results = document.getElementById("results");
    results.querySelectorAll('li').forEach(li => {
        if (li.textContent.includes(formatPhoneNumber(recommendedNumber))) {
            li.style.backgroundColor = "lightblue";
        }
    });
}

function parseMarkdown(text) {
    return text
        .replace(/\n/g, '<br>')
        .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')  // Kalın metin
        .replace(/\*(.*?)\*/g, '<em>$1</em>');            // İtalik metin
}

function closeModal() {
    document.getElementById("ai-response-modal").style.display = "none";
}

function redirectToConfirmPage(phoneNumberId) {
    window.location.href = `phone-numbers/confirm/${phoneNumberId}`;
}
