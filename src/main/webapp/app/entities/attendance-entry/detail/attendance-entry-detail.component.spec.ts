import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AttendanceEntryDetailComponent } from './attendance-entry-detail.component';

describe('AttendanceEntry Management Detail Component', () => {
  let comp: AttendanceEntryDetailComponent;
  let fixture: ComponentFixture<AttendanceEntryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AttendanceEntryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ attendanceEntry: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AttendanceEntryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AttendanceEntryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load attendanceEntry on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.attendanceEntry).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
