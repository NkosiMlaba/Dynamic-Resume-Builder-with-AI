document.getElementById('create').addEventListener('submit', async function(event) {
    event.preventDefault();
    
    const form = event.target;
    const formData = new FormData(form);
    
    try {
        const response = await fetch('http://localhost:7000/register', {
        method: 'POST',
        body: formData
        });
    
        if (response.ok) {
        const result = await response.json();
        console.log('Form submitted successfully:', result);
        } else {
        console.error('Error submitting form:', response.status, response.statusText);
        }
    } catch (error) {
        console.error('Error submitting form:', error);
    }
    });