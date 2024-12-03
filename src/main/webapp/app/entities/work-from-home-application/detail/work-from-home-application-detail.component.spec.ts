import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WorkFromHomeApplicationDetailComponent } from './work-from-home-application-detail.component';

describe('WorkFromHomeApplication Management Detail Component', () => {
  let comp: WorkFromHomeApplicationDetailComponent;
  let fixture: ComponentFixture<WorkFromHomeApplicationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WorkFromHomeApplicationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ workFromHomeApplication: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WorkFromHomeApplicationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WorkFromHomeApplicationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load workFromHomeApplication on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.workFromHomeApplication).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
