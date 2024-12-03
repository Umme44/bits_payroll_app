import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IFileTemplates } from '../../shared/legacy/legacy-model/file-templates.model';

@Component({
  selector: 'jhi-file-templates-detail',
  templateUrl: './file-templates-detail.component.html',
})
export class FileTemplatesDetailComponent implements OnInit {
  fileTemplates: IFileTemplates | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileTemplates }) => (this.fileTemplates = fileTemplates));
  }

  // byteSize(base64String: string): string {
  //   return this.dataUtils.byteSize(base64String);
  // }

  // openFile(contentType = '', base64String: string): void {
  //   this.dataUtils.openFile(contentType, base64String);
  // }

  previousState(): void {
    window.history.back();
  }
}
