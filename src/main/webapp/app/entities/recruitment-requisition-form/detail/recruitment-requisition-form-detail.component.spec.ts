import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RecruitmentRequisitionFormDetailComponent } from './recruitment-requisition-form-detail.component';

describe('RecruitmentRequisitionForm Management Detail Component', () => {
  let comp: RecruitmentRequisitionFormDetailComponent;
  let fixture: ComponentFixture<RecruitmentRequisitionFormDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RecruitmentRequisitionFormDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ recruitmentRequisitionForm: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RecruitmentRequisitionFormDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RecruitmentRequisitionFormDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load recruitmentRequisitionForm on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.recruitmentRequisitionForm).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
