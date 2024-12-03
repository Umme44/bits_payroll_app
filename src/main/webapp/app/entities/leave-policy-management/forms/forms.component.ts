import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'jhi-forms',
  templateUrl: './forms.component.html',
  styleUrls: ['./forms.component.scss']
})
export class FormsComponent implements OnInit {

  leavePolicyForm: FormGroup;


  constructor(private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.initForm();
  }

  private initForm(): void {
    this.leavePolicyForm = this.formBuilder.group({

    });
  }

  onSubmit(): void {
    if (this.leavePolicyForm.valid) {
      console.log(this.leavePolicyForm.value);
    }
  }

}
