import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { HttpResponse } from '@angular/common/http';
import { FormControl, FormGroup } from '@angular/forms';
import { swalOnRequestError, swalOnUpdatedSuccess } from '../../../shared/swal-common/swal-common';
import { EmployeeStaticFileService } from '../service/employee-static-file.service';
import { IEmployeeStaticFile } from '../employee-static-file.model';

@Component({
  selector: 'jhi-employee-static-file',
  templateUrl: './employee-id-card-upload-update.component.html',
})
export class EmployeeIdCardUploadUpdateComponent implements OnInit, OnDestroy {
  selectedFiles!: File;
  isSaving!: boolean;
  id!: number;
  employeeStaticFile!: IEmployeeStaticFile;

  createFrom = new FormGroup({
    photo: new FormControl(),
    fullName: new FormControl(),
  });

  constructor(protected employeeStaticFileService: EmployeeStaticFileService, protected router: Router, protected route: ActivatedRoute) {}

  ngOnInit(): void {
    this.getEmployeeStaticFileDTO();
  }

  ngOnDestroy(): void {}

  updateForm(employeeStaticFile: IEmployeeStaticFile): void {
    // this.createFrom.patchValue({
    //   id: employeeStaticFile.id,
    //   filePath: employeeStaticFile.filePath,
    //   employeeId: employeeStaticFile.employeeId,
    // });
  }

  getEmployeeStaticFileDTO(): void {
    this.id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.employeeStaticFileService.getEmployeeStaticFileDTO(this.id).subscribe((data: HttpResponse<IEmployeeStaticFile>) => {
      this.employeeStaticFile = data.body!;
      this.updateForm(this.employeeStaticFile);
    });
  }

  onChange(event: any): void {
    this.selectedFiles = event.target.files[0];
  }

  onSubmit(): void {
    this.employeeStaticFileService.uploadImage(this.employeeStaticFile, this.selectedFiles).subscribe(
      () => {
        swalOnUpdatedSuccess();
        this.router.navigate(['/employee-static-file/id-card-list']);
      },
      () => swalOnRequestError()
    );
  }

  back(): void {
    window.history.back();
  }
}
