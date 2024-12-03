jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { EmployeeStaticFileService } from '../service/employee-static-file.service';

import { EmployeeStaticFileDeleteDialogComponent } from './employee-static-file-delete-dialog.component';

describe('EmployeeStaticFile Management Delete Component', () => {
  let comp: EmployeeStaticFileDeleteDialogComponent;
  let fixture: ComponentFixture<EmployeeStaticFileDeleteDialogComponent>;
  let service: EmployeeStaticFileService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EmployeeStaticFileDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(EmployeeStaticFileDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EmployeeStaticFileDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EmployeeStaticFileService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
