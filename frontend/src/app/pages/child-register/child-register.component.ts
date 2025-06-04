import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-child-register',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './child-register.component.html',
  styleUrl: './child-register.component.css'
})
export class ChildRegisterComponent {
  childForm: FormGroup;

  genders = [
    { value: 'MALE', label: 'Masculino' },
    { value: 'FEMALE', label: 'Femenino' },
    { value: 'OTHER', label: 'Otro' }
  ];

  constructor(private fb: FormBuilder) {
    this.childForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      birthDate: ['', Validators.required],
      gender: ['', Validators.required],
      specialNeeds: [''],
      dni: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.childForm.valid) {
      console.log(this.childForm.value);
    } else {
      this.childForm.markAllAsTouched();
    }
  }
}
