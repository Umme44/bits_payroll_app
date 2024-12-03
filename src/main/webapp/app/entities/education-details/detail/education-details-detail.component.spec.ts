import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EducationDetailsDetailComponent } from './education-details-detail.component';

describe('EducationDetails Management Detail Component', () => {
  let comp: EducationDetailsDetailComponent;
  let fixture: ComponentFixture<EducationDetailsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EducationDetailsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ educationDetails: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EducationDetailsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EducationDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load educationDetails on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.educationDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
