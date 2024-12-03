jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AttendanceEntryService } from '../service/attendance-entry.service';

import { AttendanceEntryDeleteDialogComponent } from './attendance-entry-delete-dialog.component';

describe('AttendanceEntry Management Delete Component', () => {
  let comp: AttendanceEntryDeleteDialogComponent;
  let fixture: ComponentFixture<AttendanceEntryDeleteDialogComponent>;
  let service: AttendanceEntryService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AttendanceEntryDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(AttendanceEntryDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AttendanceEntryDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AttendanceEntryService);
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
