import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AttendanceSummaryDetailComponent } from './attendance-summary-detail.component';

describe('AttendanceSummary Management Detail Component', () => {
  let comp: AttendanceSummaryDetailComponent;
  let fixture: ComponentFixture<AttendanceSummaryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AttendanceSummaryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ attendanceSummary: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AttendanceSummaryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AttendanceSummaryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load attendanceSummary on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.attendanceSummary).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
