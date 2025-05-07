import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators, Form } from '@angular/forms';

@Component({
  selector: 'app-parent-register',
  imports: [FormBuilder],
  templateUrl: './parent-register.component.html',
  styleUrl: './parent-register.component.css'
})
export class ParentRegisterComponent {
  tutorForm: FormGroup;

  documentTypes = [
    { id: 1, name: 'DNI' },
    { id: 2, name: 'Pasaporte' },
    { id: 3, name: 'Cédula' }
  ];

  contactTypes = [
    { id: 1, name: 'Teléfono' },
    { id: 2, name: 'Email' },
    { id: 3, name: 'WhatsApp' }
  ];

  constructor(private fb: FormBuilder) {
    this.tutorForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      document: ['', Validators.required],
      documentType: ['', Validators.required],
      contacts: this.fb.array([]),
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      address: ['', Validators.required],
      postalCode: ['', Validators.required],
      city: ['', Validators.required],
      relationshipToChild: ['']
    });
  }

  get contacts(): FormArray {
    return this.tutorForm.get('contacts') as FormArray;
  }

  addContact(): void {
    this.contacts.push(this.fb.group({
      content: ['', Validators.required],
      type: ['', Validators.required],
      primary: [false]
    }));
  }

  removeContact(index: number): void {
    this.contacts.removeAt(index);
  }

  onSubmit(): void {
    if (this.tutorForm.valid) {
      console.log(this.tutorForm.value);
      
    } else {
      this.tutorForm.markAllAsTouched();
    }
  }
}
