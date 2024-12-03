import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EmploymentHistoryDetailComponent } from './employment-history-detail.component';

describe('EmploymentHistory Management Detail Component', () => {
  let comp: EmploymentHistoryDetailComponent;
  let fixture: ComponentFixture<EmploymentHistoryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmploymentHistoryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ employmentHistory: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EmploymentHistoryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EmploymentHistoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load employmentHistory on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.employmentHistory).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
