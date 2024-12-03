import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ManualAttendanceEntryDetailComponent } from './manual-attendance-entry-detail.component';

describe('ManualAttendanceEntry Management Detail Component', () => {
  let comp: ManualAttendanceEntryDetailComponent;
  let fixture: ComponentFixture<ManualAttendanceEntryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ManualAttendanceEntryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ manualAttendanceEntry: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ManualAttendanceEntryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ManualAttendanceEntryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load manualAttendanceEntry on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.manualAttendanceEntry).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
